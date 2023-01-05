package com.example.electronicgradebook.repository;

import com.example.electronicgradebook.resources.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    PrivateMessage findByReceiver(String receiver);
}
