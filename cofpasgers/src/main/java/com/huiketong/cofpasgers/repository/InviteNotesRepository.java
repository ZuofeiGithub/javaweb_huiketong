package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.InviteNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteNotesRepository extends JpaRepository<InviteNotes,Integer> {

    InviteNotes findInviteNotesByCompayId(Integer companyId);
}
