package app.clinic.shared.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;

/**
 * Controlador para funcionalidades de soporte técnico.
 * Solo accesible para el rol SOPORTE.
 */
@RestController
@RequestMapping("/support")
public class SupportController {

    /**
     * Endpoint para obtener información del sistema (solo soporte).
     */
    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        validateSupportRole();

        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("status", "Sistema operativo correctamente");
        systemInfo.put("version", "1.0.0");
        systemInfo.put("uptime", System.currentTimeMillis());
        systemInfo.put("java_version", System.getProperty("java.version"));
        systemInfo.put("os", System.getProperty("os.name"));

        return ResponseEntity.ok(systemInfo);
    }

    /**
     * Endpoint para reportar problemas técnicos (solo soporte).
     */
    @PostMapping("/report-issue")
    public ResponseEntity<Map<String, String>> reportIssue(@RequestBody Map<String, String> issueReport) {
        validateSupportRole();

        String issue = issueReport.get("issue");

        if (issue == null || issue.trim().isEmpty()) {
            throw new ValidationException("La descripción del problema es obligatoria.");
        }

        // Aquí se podría implementar lógica para guardar el reporte en BD
        // o enviar notificaciones, pero por ahora solo retornamos confirmación

        Map<String, String> response = new HashMap<>();
        response.put("message", "Problema reportado exitosamente");
        response.put("issue_id", "SUP-" + System.currentTimeMillis());
        response.put("status", "Registrado");

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener métricas de uso del sistema (solo soporte).
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        validateSupportRole();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("active_users", 0); // Placeholder
        metrics.put("total_patients", 0); // Placeholder
        metrics.put("total_appointments", 0); // Placeholder
        metrics.put("system_load", "Normal");

        return ResponseEntity.ok(metrics);
    }

    /**
     * Valida que el usuario tenga rol SOPORTE.
     */
    private void validateSupportRole() {
        String roleStr = SecurityUtils.getCurrentRole();
        if (roleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role role = Role.valueOf(roleStr.toUpperCase());
        if (role != Role.SOPORTE) {
            throw new ValidationException("Solo el personal de soporte puede acceder a estas funcionalidades.");
        }
    }

}