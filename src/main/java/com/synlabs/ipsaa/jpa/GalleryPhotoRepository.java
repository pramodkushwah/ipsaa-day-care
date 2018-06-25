package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.GalleryPhoto;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.student.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryPhotoRepository extends JpaRepository<GalleryPhoto, Long>
{

  @Query(value =
      "select p from GalleryPhoto p "
          + "where p.center = :center "
          + "and  (p.student = :student or p.student is null)"
          + "order by p.createdDate desc ")
  List<GalleryPhoto> findPhotoForPP(@Param("center") Center center,
                                    @Param("student") Student student,
                                    Pageable pageable);


  @Query(value =
      "select p from GalleryPhoto p "
          + "where p.center = :center "
          + "and  (p.student = :student or p.student is null)"
          + "order by p.createdDate desc ")
  List<GalleryPhoto> findPhotoForPP(@Param("center") Center center,
                                    @Param("student") Student student);

}
