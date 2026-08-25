package Veterinaria.persistencia;

import Veterinaria.Dueño;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatosClinica {
    private final List<Dueño> duenos;

    public DatosClinica() {
        this.duenos = new ArrayList<>();
    }

    public DatosClinica(List<Dueño> duenos) {
        this.duenos = new ArrayList<>(duenos);
    }

    public List<Dueño> getDuenos() {
        return Collections.unmodifiableList(duenos);
    }
}
