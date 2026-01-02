package challenge.concurrency.dto;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
   
    @Column
    private String name;
    @Column
    private String line;  

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", line=" + line + '}';
    }       
}
