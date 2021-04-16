package xyz.mynt.delivery.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xyz.mynt.delivery.model.DeliveryCostRule;

@Slf4j
@Configuration
public class DeliveryCostRuleConfig {

    @Getter
    private Map<Integer, DeliveryCostRule> deliveryCostRuleMap;

    @Value("${delivery.cost.config.version}")
    private String deliveryCostConfigVersion;

    @PostConstruct
    private void buildDeliveryCostRuleMap() {
        List<DeliveryCostRule> deliveryCostRules = Collections.emptyList();
        StringBuilder configFileName = new StringBuilder("delivery-cost-config-").append(deliveryCostConfigVersion).append(".json");
        log.info("delivery cost configFileName: {}", configFileName);
        try {
            ObjectMapper mapper = new ObjectMapper();
            deliveryCostRules = mapper
                .readValue(Objects.requireNonNull(DeliveryCostRuleConfig.class.getClassLoader().getResourceAsStream(configFileName.toString())),
                    new TypeReference<List<DeliveryCostRule>>() {
                    });
        } catch (IOException ioe) {
            log.error("IOException encountered parsing: {} : {}", configFileName, ioe.getMessage());
        }

        if (CollectionUtils.isNotEmpty(deliveryCostRules)) {
            deliveryCostRuleMap = Collections.unmodifiableMap(deliveryCostRules.stream()
                .collect(Collectors.toMap(DeliveryCostRule::getPriority, Function.identity(), (rule1, rule2) -> rule1, TreeMap::new)));
        }

        log.info("deliveryCostRuleMap: {}", deliveryCostRuleMap);
    }
}
