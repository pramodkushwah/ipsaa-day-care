package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by itrs on 5/2/2017.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long>
{

  PasswordResetToken findByToken(String token);
}
