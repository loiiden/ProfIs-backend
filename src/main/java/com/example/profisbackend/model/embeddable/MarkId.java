package com.example.profisbackend.model.embeddable;

import com.example.profisbackend.enums.MarkType;
import java.io.Serializable;
import java.util.Objects;

public class MarkId implements Serializable {
    private MarkType type;
    private Long scientificWorkId;
    private Long evaluatorId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkId markId = (MarkId) o;
        return Objects.equals(evaluatorId, markId.evaluatorId) &&
                Objects.equals(scientificWorkId, markId.scientificWorkId) &&
                type == markId.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluatorId, scientificWorkId, type);
    }
}
