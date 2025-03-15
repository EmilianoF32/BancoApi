package com.example.Api.Model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.Api.Model.Cuenta;
import com.example.Api.Model.Roles;
import com.example.Api.Repository.CuentaRepository;
import com.example.Api.Repository.RoleRepository;
import com.example.Api.Repository.TipoCuenta;
import com.example.Api.Repository.RoleName;
import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final CuentaRepository cuentaRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(CuentaRepository cuentaRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.cuentaRepository = cuentaRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Verificar si el rol ADMIN existe, si no, crearlo
        Optional<Roles> adminRole = roleRepository.findById(1L);
        if (adminRole.isEmpty()) {
            Roles nuevoAdminRole = new Roles();
            nuevoAdminRole.setId(1L);
            nuevoAdminRole.setName(RoleName.ADMIN);
            roleRepository.save(nuevoAdminRole);
            adminRole = Optional.of(nuevoAdminRole);
        }

        // Verificar si ya existe un administrador
        if (cuentaRepository.count() == 0) {
            Cuenta admin = new Cuenta();
            admin.setNombre("Admin");
            admin.setApellidoPaterno("Principal");
            admin.setApellidoMaterno("Sistema");
            admin.setCurp("ADMN650101HDFXXX01");
            admin.setCorreoElectronico("admin@sistema.com");
            admin.setContrasena(passwordEncoder.encode("Admin123")); // Encriptada
            admin.setNumeroTarjeta("123456789012345A");
            admin.setTipoCuenta(TipoCuenta.ADMIN);
            admin.setRole(adminRole.get());

            cuentaRepository.save(admin);
            System.out.println("✅ Administrador inicial creado con éxito.");
        }
    }
}