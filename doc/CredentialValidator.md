# Buat Constraint Validation Annotation

Anotasi ini digunakan sebagai tanda, bahwa instance dari object yang dipakaikan anotasi ini bisa divalidasi.

***Catat ini**, instance dari object, bukan hanya Polymorphism dari Credential.

```java

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CredentialValidator.class)
public @interface ValidCredential {

    String message() default "Invalid Credential";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
```

# Buat Credential Interface

Dikarenakan Credential itu memiliki berbagai macam bentuk, seperti JWT, Basic Auth, OAuth, dan lain-lain, maka kita
perlu membuat sebuah interface yang akan digunakan untuk implementasi konsep polymorphism.

````java

@ValidCredential
public interface Credential {
}
````

# Buat Implementasi Credential (Polymorphism)

Karena Credential hanyalah Interface, kita perlu membuat bentuk konkritnya, salah satunya bentuknya adalah bentuk
Username dan Password, maka kita buat Record ini.

```java
public record UsernamePasswordCredential(String username, String password) implements Credential {
}
```

# Buat Service Bean Untuk Validasi Credential

Disini kita gunakan mock service saja.

Kita buat satu method untuk melakukan validasi terhadap UsernamePasswordCredential.

```java

@RequestScoped
public class CredentialService {

    private Set<UsernamePasswordCredential> usernamePasswordCredentialsSet;

    @PostConstruct
    public void init() {
        usernamePasswordCredentialsSet = Set.of(
                new UsernamePasswordCredential("admin", "admin"),
                new UsernamePasswordCredential("user", "user")
        );

        LoggingManager log = new LoggingManager(this);
        log.logInfo("CredentialService and Content was initialized");
    }

    public boolean validateUsernamePasswordCredential(UsernamePasswordCredential usernamePasswordCredential) {

        String username = usernamePasswordCredential.username();
        String password = usernamePasswordCredential.password();

        return usernamePasswordCredentialsSet.stream()
                .anyMatch(credential ->
                        credential.username()
                                .equals(username)
                                && credential.password()
                                .equals(password));

    }
}
```

# Buat Class Untuk Validatornya

Class ini akan digunakan untuk melakukan validasi terhadap instance dari Credential.
Class ini harus implement ConstraintValidator dimana A adalah nama anotasi untuk constraint dan T adalah Interface
Credential.

Inject Service Bean yang digunakan untuk melakukan validasi.
Lalu buat logika sesuai dengan apa yang kalian pikirkan untuk melakukan validasi.

```java
public class CredentialValidator implements ConstraintValidator<ValidCredential, Credential> {

    @Inject
    CredentialService credentialService;

    @Override
    public boolean isValid(Credential value, ConstraintValidatorContext context) {

        if (value instanceof UsernamePasswordCredential upc) {
            return credentialService
                    .validateUsernamePasswordCredential(upc);
        }

        return false;

    }
}

```

# Buat Universal Validation Service Bean

Bean ini digunakan untuk melakukan validasi, lalu mendapatkan message violation apa saja yang terjadi. Jika tidak ada
violation yang terjadi, maka akan mengembalikan null.

```java
public class ValidationService {

    @Inject
    private ValidatorFactory validatorFactory;

    public String getViolationMessages(Object object) {

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(violation -> {

                        String violationSource = violation.getPropertyPath().toString();

                        // Jika blank maka Level class yang invalid
                        // Jika berisi maka level Field, lanjutkan
                        if (violationSource.isBlank()) {
                            violationSource = "Entity " + violation.getInvalidValue()
                                    .getClass().getSimpleName();

                        } else {
                            violationSource = "Field " + violationSource;
                        }

                        // [field/entity] : [message]
                        return String.format("[%s] : %s",
                                violationSource,
                                violation.getMessage());

                    })
                    .collect(Collectors.joining(", "));
        } else {
            return null;
        }

    }
}

```

# Buat REST nya

Inject Validation Service Bean ke dalam Resource REST.
Lakukan validasi menggunakan ValidationService.
Jika ada violation, maka proses violation tersebut.

```java

@Path("/validation")
@SafeHttpInvoke
public class ValidationResource {

    @Inject
    private ValidationService validationService;

    @POST
    @Path("/basic-auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response basicAuthentication(
            UsernamePasswordCredential usernamePasswordCredential
    ) {

        String violationMessages = validationService.getViolationMessages(usernamePasswordCredential);

        if (violationMessages != null) {
            GenericResponse payload = new GenericResponse("400", violationMessages, null);
            return Response.status(Response.Status.BAD_REQUEST).entity(payload).build();
        }

        return Response.ok().entity("Credential Valid").build();
    }
}
```