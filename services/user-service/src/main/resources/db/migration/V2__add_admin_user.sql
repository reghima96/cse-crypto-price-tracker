INSERT INTO users (id, name, email, password, roles)
VALUES
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
    (uuid_generate_v4(), 'Admin', 'admin@cse.com', '{bcrypt}$2a$10$FKB6IWgpL9WYms8bSGRsR.VP3wA5mSnzjv/pbT3A16KiWEMyYWXMy', 'ADMIN');
