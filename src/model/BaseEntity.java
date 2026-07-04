package model;

public abstract class BaseEntity {
    protected String id;

    public BaseEntity() {}
    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Id tidak boleh kosong!");
        this.id = id;
    }

    public abstract String getInfo();

    @Override
    public String toString() {
        return getInfo();
    }
}