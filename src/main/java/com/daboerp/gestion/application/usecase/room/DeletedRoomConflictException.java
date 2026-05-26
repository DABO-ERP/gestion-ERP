package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ApplicationException;

public class DeletedRoomConflictException extends ApplicationException {

    private final Integer roomNumber;
    private final String roomId;

    public DeletedRoomConflictException(Integer roomNumber, String roomId) {
        super(String.format("La habitación N° %d fue eliminada previamente. ¿Desea restaurarla?", roomNumber));
        this.roomNumber = roomNumber;
        this.roomId = roomId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public String getRoomId() {
        return roomId;
    }
}
