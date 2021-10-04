package com.depop.cx.drc.workflow.listener;

import org.camunda.bpm.engine.delegate.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * A global task/execution listener that listens for {@link TaskListener#EVENTNAME_CREATE} events
 * and copies any camunda extension properties from the task definition into local variables
 * on the task instance.
 *
 * This lets us access the extension properties via the rest API for both active and historical
 * tasks.
 */
public class CamundaExtensionsVariableBuilder implements TaskListener, ExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(CamundaExtensionsVariableBuilder.class);

    @Override
    public void notify(final DelegateTask delegateTask) {
        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            buildExtensionVariables(delegateTask, delegateTask.getName());
        }
    }

    @Override
    public void notify(final DelegateExecution execution) {
        if (ExecutionListener.EVENTNAME_START.equals(execution.getEventName())) {
            buildExtensionVariables(execution, execution.getCurrentActivityName());
        }
    }

    protected <T extends BpmnModelExecutionContext & VariableScope> void buildExtensionVariables(T element, final String elementName) {
        final var elementDefinition = element.getBpmnModelElementInstance();
        final var extensions = elementDefinition.getExtensionElements();
        if(extensions != null) {

            final var properties = extensions.getElementsQuery()
                    .filterByType(CamundaProperties.class)
                    .list();

            if (!properties.isEmpty()) {
                properties.stream().flatMap(p -> p.getCamundaProperties().stream()).forEach(property -> {
                    LOG.info("Creating local variable for extension property [{}:{}] on element [{}].",
                            property.getCamundaName(), property.getCamundaValue(), elementName);
                    element.setVariableLocal(property.getCamundaName(), property.getCamundaValue());
                });
            }
        }
    }
}
