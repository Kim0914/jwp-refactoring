package kitchenpos.ui;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.application.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);
        URI uri = URI.create("/api/table-groups/" + tableGroupResponse.getId());
        return ResponseEntity.created(uri).body(tableGroupResponse);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
