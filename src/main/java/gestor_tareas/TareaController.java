package gestor_tareas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    @GetMapping("/")
    public String listarTareas(@RequestParam(name = "filtro", required = false) String filtro, Model model) {
        List<Tarea> tareas;

        if ("pendientes".equals(filtro)) {
            tareas = tareaRepository.findByCompletada(false);
        } else if ("completadas".equals(filtro)) {
            tareas = tareaRepository.findByCompletada(true);
        } else {
            tareas = tareaRepository.findAll();
        }

        model.addAttribute("tareas", tareas);
        model.addAttribute("filtroActual", filtro);
        return "index";
    }

    @PostMapping("/guardar")
    public String guardarTarea(@RequestParam("descripcion") String descripcion) {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            Tarea tarea = new Tarea();
            tarea.setDescripcion(descripcion.trim());
            tarea.setCompletada(false);
            tareaRepository.save(tarea);
        }
        return "redirect:/";
    }

    @GetMapping("/completar/{id}")
    public String cambiarEstado(@PathVariable("id") Long id) {
        tareaRepository.findById(id).ifPresent(tarea -> {
            tarea.setCompletada(!tarea.isCompletada());
            tareaRepository.save(tarea);
        });
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable("id") Long id) {
        tareaRepository.deleteById(id);
        return "redirect:/";
    }
}
