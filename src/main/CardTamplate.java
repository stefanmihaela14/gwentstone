package main;

public interface CardTamplate {
    // desi ar trebui sa ne intoarca ceva de tipul card... un fel de structura... un pretty-print json sau ceva de genul asta

    public void setMana(int mana);
    public void getMana();

    public void setDescription(String description);
    public void getDescription();

    public void setColors(String colors);
    public void getColors();

    public void setName(String name);
    public void getName();

}
