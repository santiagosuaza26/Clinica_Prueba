package app.clinic.appointment.application.mapper;

import app.clinic.appointment.application.dto.AppointmentRequestDto;
import app.clinic.appointment.application.dto.AppointmentResponseDto;
import app.clinic.appointment.domain.model.Appointment;

public class AppointmentMapper {

    public static Appointment toDomain(AppointmentRequestDto dto) {
        return new Appointment(
                null, // id will be set by repository
                dto.patientCedula(),
                dto.doctorCedula(),
                dto.dateTime(),
                dto.reason(),
                "SCHEDULED" // default status
        );
    }

    public static AppointmentResponseDto toResponse(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getPatientCedula(),
                appointment.getDoctorCedula(),
                appointment.getDateTime(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }
}