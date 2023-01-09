package com.example.electronicgradebook.repository;

import com.example.electronicgradebook.resources.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findByReceiver(String receiver);
}
