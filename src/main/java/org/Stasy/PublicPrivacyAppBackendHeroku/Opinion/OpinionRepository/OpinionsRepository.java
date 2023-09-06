//package org.Stasy.PublicPrivacyAppBackendHeroku.repository;
//
//import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@EnableJpaRepositories
//@Repository
//public interface OpinionsRepository extends JpaRepository<Opinion, Long> {
//
//        Opinion findOpinionById(Integer id);
//        List<Opinion> findOpinionByCollaboratorName(String username);
//
//        String findUsernameById(Long id);//return String==>because we are returning name!
//
//        String findCollaboratorNameById(Integer id);
//
//        void deleteById(Integer id);
//}
package org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface OpinionsRepository extends JpaRepository<Opinion, Long> {

        Opinion findOpinionById(Long id);
        //List<Opinion> findOpinionByCollaboratorName(String username);
        List<Opinion> findOpinionByCollaboratorNameOrderByUpdatedAtDesc(String username);

        String findUsernameById(Long id);//return String==>because we are returning name!

        String findCollaboratorNameById(Long id);

        void deleteById(Long id);

        List<Opinion> findAllByOrderByUpdatedAtDesc();

        void deleteAllByCollaboratorName(String username);
}