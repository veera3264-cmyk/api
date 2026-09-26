package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

<<<<<<< HEAD

=======
>>>>>>> b64fa8e5b9df9780bb6e727fab38330ddd1f851f
    User findByEmail(String email);
}