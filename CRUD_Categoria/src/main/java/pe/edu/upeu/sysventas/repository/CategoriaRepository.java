package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Categoria;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

/**
 * Repositorio de categorías con persistencia local en un archivo CSV.
 * Los registros se conservan aunque se cierre y vuelva a abrir la aplicación.
 */
public class CategoriaRepository extends AbstractJpaRepository<Categoria, Long> {
    private long sequence = 1;
    private final Path archivo = Paths.get(
            System.getProperty("user.home"), ".crud_categoria", "categorias.csv");

    public CategoriaRepository() {
        cargarDesdeArchivo();
    }

    @Override
    protected Long getId(Categoria entity) {
        return entity.getIdCategoria();
    }

    @Override
    protected void setId(Categoria entity, Long id) {
        entity.setIdCategoria(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    @Override
    public synchronized Categoria save(Categoria entity) {
        if (getId(entity) == null) {
            setId(entity, generateId());
        } else if (entity.getIdCategoria() >= sequence) {
            sequence = entity.getIdCategoria() + 1;
        }
        super.save(entity);
        guardarEnArchivo();
        return entity;
    }

    @Override
    public synchronized Categoria update(Categoria entity) {
        Categoria actualizado = super.update(entity);
        guardarEnArchivo();
        return actualizado;
    }

    @Override
    public synchronized void deleteById(Long id) {
        super.deleteById(id);
        guardarEnArchivo();
    }

    /**
     * Carga las categorías guardadas en el equipo.
     * El nombre se codifica en Base64 para admitir tildes y separadores.
     */
    private void cargarDesdeArchivo() {
        if (!Files.exists(archivo)) {
            return;
        }

        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea.isBlank()) continue;
                String[] partes = linea.split(",", 2);
                if (partes.length != 2) continue;

                try {
                    Long id = Long.parseLong(partes[0]);
                    String nombre = new String(
                            Base64.getDecoder().decode(partes[1]), StandardCharsets.UTF_8);
                    data.add(new Categoria(id, nombre));
                    if (id >= sequence) sequence = id + 1;
                } catch (IllegalArgumentException ex) {
                    System.err.println("Se omitió una línea inválida en " + archivo);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No se pudieron cargar las categorías guardadas en: " + archivo, ex);
        }
    }

    /**
     * Guarda todos los registros en disco. Primero escribe un archivo temporal
     * y después lo reemplaza para reducir el riesgo de perder datos.
     */
    private void guardarEnArchivo() {
        try {
            Files.createDirectories(archivo.getParent());
            Path temporal = archivo.resolveSibling("categorias.csv.tmp");

            List<String> lineas = data.stream()
                    .map(categoria -> categoria.getIdCategoria() + ","
                            + Base64.getEncoder().encodeToString(
                                    categoria.getNombre().getBytes(StandardCharsets.UTF_8)))
                    .toList();

            Files.write(temporal, lineas, StandardCharsets.UTF_8);
            try {
                Files.move(temporal, archivo,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException noAtomico) {
                Files.move(temporal, archivo,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No se pudieron guardar las categorías en: " + archivo, ex);
        }
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new Categoria(null, "Urbana"));
            save(new Categoria(null, "Running"));
            save(new Categoria(null, "Básquet"));
        }
    }
}
