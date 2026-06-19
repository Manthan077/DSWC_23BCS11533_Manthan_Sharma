import jakarta.persistence.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "authors")
class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    private String username;

    @OneToMany
    @JoinColumn(name = "author_id")
    private List<MediaAsset> mediaAssets;
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class MediaAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assetId;

    private String title;
}

@Entity
class VideoAsset extends MediaAsset {

    private String resolution;

    public String getResolution() {
        return resolution;
    }
}

@Entity
class PodcastAsset extends MediaAsset {

    private int episodeNumber;
}

interface AuthorRepository
        extends JpaRepository<Author, Long> {

    @Query("""
        SELECT m
        FROM MediaAsset m
        JOIN Author a
        ON m MEMBER OF a.mediaAssets
        WHERE a.authorId = :authorId
        AND TREAT(m AS VideoAsset).resolution = :resolution
    """)
    List<MediaAsset> find4KVideosByAuthor(
            Long authorId,
            String resolution
    );

    @EntityGraph(attributePaths = "mediaAssets")
    Optional<Author> findByUsername(
            String username
    );
}

public class StreamCastContentDeliveryNetwork {

    public static void main(String[] args) {

        System.out.println(
                "StreamCast Content Delivery Network");
    }
}