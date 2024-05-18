package kr.jay.migration.domain.recent.campaign;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import kr.jay.migration.domain.recent.MigratedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * LegacyCampaign
 *
 * @author jaypark
 * @version 1.0.0
 * @since 5/16/24
 */
@Entity
@NoArgsConstructor
@Getter
public class RecentCampaign implements MigratedEntity {

    @Id
    private Long id;
    private String name;
    private Long userId;
    private Long budget;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime migratedAt;

}
