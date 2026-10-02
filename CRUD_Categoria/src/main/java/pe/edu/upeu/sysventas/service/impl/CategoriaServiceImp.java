package pe.edu.upeu.sysventas.service.impl;
import java.util.ArrayList;
import java.util.List;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.model.Categoria;
import pe.edu.upeu.sysventas.repository.*;
import pe.edu.upeu.sysventas.service.ICategoriaService;

public class CategoriaServiceImp extends CrudGenericoServiceImp<Categoria, Long> implements ICategoriaService {
    private final CategoriaRepository repositorio;
    public CategoriaServiceImp(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
        repositorio.seedData();
    }
    @Override protected ICrudGenericoRepository<Categoria, Long> getRepo() { return repositorio; }
    private void validar(Categoria entidad, Long idActual) {
        if (entidad == null || entidad.getNombre() == null || entidad.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("Escribe un nombre.");
        String nombre = entidad.getNombre().trim();
        for (Categoria existente : findAll()) {
            if (!existente.getIdCategoria().equals(idActual) && existente.getNombre().equalsIgnoreCase(nombre))
                throw new IllegalArgumentException("Ya existe un registro con ese nombre.");
        }
        entidad.setNombre(nombre);
    }
    @Override public Categoria save(Categoria entidad) {
        validar(entidad, null);
        entidad.setIdCategoria(null);
        return super.save(entidad);
    }
    @Override public Categoria update(Long id, Categoria entidad) {
        findById(id);
        validar(entidad, id);
        entidad.setIdCategoria(id);
        return super.update(id, entidad);
    }
    @Override public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> lista = new ArrayList<>();
        for (Categoria entidad : findAll())
            lista.add(new ComboBoxOption(String.valueOf(entidad.getIdCategoria()), entidad.getNombre()));
        return lista;
    }
}
