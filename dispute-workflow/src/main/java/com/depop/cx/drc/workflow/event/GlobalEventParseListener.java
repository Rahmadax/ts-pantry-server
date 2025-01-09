/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.depop.cx.drc.workflow.event;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.variable.VariableDeclaration;

import java.util.List;

/***
 * {@link BpmnParseListener} implementation that registers the
 * {@link ExecutionListener} and {@link TaskListener} provided in the
 * constructor with every event, on every element, in every process.
 * It is the responsibility of the {@link ExecutionListener} and {@link TaskListener}
 * to determine if the events are relevant.
 */
public class GlobalEventParseListener implements BpmnParseListener {

    private final ExecutionListener executionListener;
    private final TaskListener taskListener;

    public GlobalEventParseListener(final ExecutionListener executionListener, final TaskListener taskListener) {
        this.executionListener = executionListener;
        this.taskListener = taskListener;
    }

    protected void addEndEventListener(final ScopeImpl activity) {
        activity.addListener(ExecutionListener.EVENTNAME_END, executionListener);
    }

    protected void addStartEventListener(final ScopeImpl activity) {
        activity.addListener(ExecutionListener.EVENTNAME_START, executionListener);
    }

    protected void addTakeEventListener(final TransitionImpl transition) {
        transition.addListener(ExecutionListener.EVENTNAME_TAKE, executionListener);
    }

    protected void addTaskAssignmentListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, taskListener);
    }

    protected void addTaskCreateListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, taskListener);
    }

    protected void addTaskCompleteListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, taskListener);
    }

    protected void addTaskUpdateListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_UPDATE, taskListener);
    }

    protected void addTaskDeleteListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, taskListener);
    }

    // BpmnParseListener implementation /////////////////////////////////////////////////////////

    @Override
    public void parseProcess(final Element processElement, final ProcessDefinitionEntity processDefinition) {
        addStartEventListener(processDefinition);
        addEndEventListener(processDefinition);
    }

    @Override
    public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl startEventActivity) {
        addStartEventListener(startEventActivity);
        addEndEventListener(startEventActivity);
    }

    @Override
    public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseInclusiveGateway(final Element inclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseParallelGateway(final Element parallelGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseManualTask(final Element manualTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
        final var activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
        final var taskDefinition = activityBehavior.getTaskDefinition();
        addTaskCreateListeners(taskDefinition);
        addTaskAssignmentListeners(taskDefinition);
        addTaskCompleteListeners(taskDefinition);
        addTaskUpdateListeners(taskDefinition);
        addTaskDeleteListeners(taskDefinition);
    }

    @Override
    public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseBoundaryTimerEventDefinition(final Element timerEventDefinition, final boolean interrupting, final ActivityImpl timerActivity) {
        // start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBoundaryErrorEventDefinition(final Element errorEventDefinition, final boolean interrupting, final ActivityImpl activity, final ActivityImpl nestedErrorEventActivity) {
        // start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void parseProperty(final Element propertyElement, final VariableDeclaration variableDeclaration, final ActivityImpl activity) {
        // Nothing to do.
    }

    @Override
    public void parseSequenceFlow(final Element sequenceFlowElement, final ScopeImpl scopeElement, final TransitionImpl transition) {
        addTakeEventListener(transition);
    }

    @Override
    public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseMultiInstanceLoopCharacteristics(final Element activityElement, final Element multiInstanceLoopCharacteristicsElement, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseRootElement(final Element rootElement, final List<ProcessDefinitionEntity> processDefinitions) {
        // Nothing to do.
    }

    @Override
    public void parseIntermediateTimerEventDefinition(final Element timerEventDefinition, final ActivityImpl timerActivity) {
        // start and end event listener are set by parseIntermediateCatchEvent()
    }

    @Override
    public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(final Element signalEventDefinition, final ActivityImpl signalActivity) {
        // start and end event listener are set by parseIntermediateCatchEvent()
    }

    @Override
    public void parseBoundarySignalEventDefinition(final Element signalEventDefinition, final boolean interrupting, final ActivityImpl signalActivity) {
        // start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseEventBasedGateway(final Element eventBasedGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseCompensateEventDefinition(final Element compensateEventDefinition, final ActivityImpl compensationActivity) {
        // Nothing to do.
    }

    @Override
    public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    @SuppressWarnings("java:S4144")
    public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseIntermediateMessageCatchEventDefinition(final Element messageEventDefinition, final ActivityImpl nestedActivity) {
        // Nothing to do.
    }

    @Override
    public void parseBoundaryMessageEventDefinition(final Element element, final boolean interrupting, final ActivityImpl messageActivity) {
        // Nothing to do.
    }

    @Override
    public void parseBoundaryEscalationEventDefinition(final Element escalationEventDefinition, final boolean interrupting, final ActivityImpl boundaryEventActivity) {
        // Nothing to do.
    }

    @Override
    public void parseBoundaryConditionalEventDefinition(final Element element, final boolean interrupting, final ActivityImpl conditionalActivity) {
        // Nothing to do.
    }

    @Override
    public void parseIntermediateConditionalEventDefinition(final Element conditionalEventDefinition, final ActivityImpl conditionalActivity) {
        // Nothing to do.
    }

    public void parseConditionalStartEventForEventSubprocess(final Element element, final ActivityImpl conditionalActivity, final boolean interrupting) {
        // Nothing to do.
    }

    @Override
    public void parseIoMapping(Element element, ActivityImpl activity, IoMapping ioMapping) {
        // Nothing to do.
    }
}
