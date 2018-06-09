package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Initial process storage representation.
 *
 * @author Marcos Barbero
 */
@Entity
@Table(name = "initial_process")
public class InitialProcess implements Serializable {

    private static final long serialVersionUID = -1629623023107819784L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InitialProcessStatus status;

    public InitialProcess() {
    }

    public InitialProcess(Long id, InitialProcessStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InitialProcessStatus getStatus() {
        return status;
    }

    public void setStatus(InitialProcessStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InitialProcess{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
