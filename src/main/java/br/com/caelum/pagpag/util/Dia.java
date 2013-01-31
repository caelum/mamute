package br.com.caelum.pagpag.util;

import java.util.Calendar;

public class Dia {

    private final Calendar data;

    public Dia(Calendar data) {
        this.data = (Calendar) data.clone();
    }

    public Dia() {
        this.data = Calendar.getInstance();
    }

    public Dia anteOntem() {
        return this.subitrairDias(2);
    }

    public Dia ontem() {
        return subitrairDias(1);
    }

    public Dia somaDias(int dias) {
        this.data.add(Calendar.DAY_OF_MONTH, dias);
        return this;
    }

    public Dia subitrairDias(int dias) {
        return this.somaDias(dias * -1);
    }

    public Dia fimDoDia() {
        this.data.set(Calendar.HOUR_OF_DAY, 23);
        this.data.set(Calendar.MINUTE, 59);
        this.data.set(Calendar.SECOND, 59);
        return this;
    }

    public Dia comecoDoDia() {
        this.data.set(Calendar.HOUR_OF_DAY, 0);
        this.data.set(Calendar.MINUTE, 0);
        this.data.set(Calendar.SECOND, 0);
        return this;
    }

    public Calendar getData() {
        return (Calendar) data.clone();
    }

	public Dia subtrairMes(int i) {
		data.add(Calendar.MONTH, -i);
		return this;
	}
}
