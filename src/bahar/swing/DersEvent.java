package bahar.swing;

public class DersEvent {

    boolean dersSonlandi;
    boolean dersSonucuKapandi;

    public DersEvent(boolean dersSonlandi) {
        this.dersSonlandi = dersSonlandi;
    }

    public DersEvent(boolean dersSonlandi, boolean dersSonucuKapandi) {
        this.dersSonlandi = dersSonlandi;
        this.dersSonucuKapandi = dersSonucuKapandi;
    }
}
