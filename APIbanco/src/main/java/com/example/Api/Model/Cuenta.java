package com.example.Api.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.util.Random;
import com.example.Api.Repository.TipoCuenta;
import lombok.*;


@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"curp", "tipoCuenta"}))
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Relación con SaldoCuenta (One-to-One)
    @OneToOne(mappedBy = "cuenta")
    private SaldoCuenta saldoCuenta;  // Relación con la entidad SaldoCuenta
    
    @NotNull(message = "El Nombre es obligatorio.")
    private String nombre;

    @NotNull(message = "El Apellido paterno es obligatorio.")
    private String apellidoPaterno;

    @NotNull(message = "El Apellido materno es obligatorio.")
    private String apellidoMaterno;

    @NotNull(message = "El CURP es obligatorio.")
    @Size(min = 18, max = 18, message = "El CURP debe tener 18 caracteres.")
    @Pattern(
    	    regexp = "^[A-Z]{4}[0-9]{6}[HM]{1}[A-Z]{2}[BCDFGHJKLMNÑPQRSTVWXYZ]{3}[0-9A-Z]{2}$",
    	    message = "El CURP no tiene un formato válido."
    	)
    private String curp;

    @Column(unique = true)
    @NotNull(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido")
    private String correoElectronico;
    
    @NotNull(message = "Contraseña obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String contrasena;

    private String numeroTarjeta;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @PersistenceContext
    private transient EntityManager entityManager;

    public Cuenta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }
    // Getter para acceder al saldo de la cuenta
    public SaldoCuenta getSaldoCuenta() {
        return saldoCuenta;
    }

    // Setter si necesitas asignar el saldoCuenta (aunque no suele ser necesario)
    public void setSaldoCuenta(SaldoCuenta saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Generar los primeros 15 dígitos
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10)); // Genera un número entre 0-9
        }
        // Agregar la letra correspondiente según el tipo de cuenta
        if (this.tipoCuenta == TipoCuenta.CREDITO) {
            sb.append('C'); // Para cuentas de tipo CREDITO
        } else if (this.tipoCuenta == TipoCuenta.DEBITO) {
            sb.append('D'); // Para cuentas de tipo DEBITO
        } else if (this.tipoCuenta == TipoCuenta.ADMIN) {
            sb.append('A'); // Para cuentas de tipo ADMIN
        }
        return sb.toString(); // Retorna el número de tarjeta completo
    }
}
