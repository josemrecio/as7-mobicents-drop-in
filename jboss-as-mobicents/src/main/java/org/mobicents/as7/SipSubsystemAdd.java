/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.as7;

import java.util.List;
import java.util.Locale;

import javax.management.MBeanServer;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.controller.services.path.AbstractPathService;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.mobicents.as7.deployment.AttachSipServerServiceProcessor;
import org.mobicents.as7.deployment.SipAnnotationDeploymentProcessor;
import org.mobicents.as7.deployment.SipContextFactoryDeploymentProcessor;
import org.mobicents.as7.deployment.SipParsingDeploymentProcessor;

/**
 * Adds the web subsystem.
 *
 * @author Emanuel Muckenhuber
 */
class SipSubsystemAdd extends AbstractBoottimeAddStepHandler implements DescriptionProvider {

    static int PARSE_SIP_DEPLOYMENT_PRIORITY = 0x4000;
    static int SIP_ANNOTATION_DEPLOYMENT_PRIORITY = 0x5000;
    static int SIP_CONTEXT_FACTORY_DEPLOYMENT_PRIORITY = 0x6000;

    static final SipSubsystemAdd INSTANCE = new SipSubsystemAdd();
//    private static final String DEFAULT_VIRTUAL_SERVER = "default-host";
//    private static final boolean DEFAULT_NATIVE = true;
    private static final String TEMP_DIR = "jboss.server.temp.dir";

    private SipSubsystemAdd() {
        //
    }

    @Override
    protected void populateModel(ModelNode operation, final Resource resource) {
        SipConfigurationHandlerUtils.initializeConfiguration(resource, operation);
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) {
    }

    @Override
    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
                                   ServiceVerificationHandler verificationHandler,
                                   List<ServiceController<?>> newControllers) throws OperationFailedException {
//        final ModelNode config = operation.get(Constants.CONTAINER_CONFIG);
//        final String defaultVirtualServer = operation.hasDefined(Constants.DEFAULT_VIRTUAL_SERVER) ?
//                operation.get(Constants.DEFAULT_VIRTUAL_SERVER).asString() : DEFAULT_VIRTUAL_SERVER;
//        final boolean useNative = operation.hasDefined(Constants.NATIVE) ?
//                operation.get(Constants.NATIVE).asBoolean() : DEFAULT_NATIVE;
        final String instanceId = operation.hasDefined(Constants.INSTANCE_ID) ? operation.get(
                Constants.INSTANCE_ID).asString() : null;

        final SipServerService service = new SipServerService(/*defaultVirtualServer, useNative, */instanceId);
        newControllers.add(context.getServiceTarget().addService(SipSubsystemServices.JBOSS_SIP, service)
                .addDependency(AbstractPathService.pathNameOf(TEMP_DIR), String.class, service.getPathInjector())
                .addDependency(DependencyType.OPTIONAL, ServiceName.JBOSS.append("mbean", "server"), MBeanServer.class, service.getMbeanServer())
                .setInitialMode(Mode.ON_DEMAND)
                .install());

        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            protected void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(Phase.PARSE, PARSE_SIP_DEPLOYMENT_PRIORITY, new SipParsingDeploymentProcessor());
                processorTarget.addDeploymentProcessor(Phase.PARSE, SIP_ANNOTATION_DEPLOYMENT_PRIORITY, new SipAnnotationDeploymentProcessor());
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, SIP_CONTEXT_FACTORY_DEPLOYMENT_PRIORITY -1 , new AttachSipServerServiceProcessor(service));
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, SIP_CONTEXT_FACTORY_DEPLOYMENT_PRIORITY, SipContextFactoryDeploymentProcessor.INSTANCE);

//                // Add the SIP specific deployment processor
//                processorTarget.addDeploymentProcessor(Phase.PARSE, DEPLOYMENT_PROCESS_PRIORITY, SipMetaDataDeploymentProcessor.INSTANCE);
//                // Add the context in a different phase
//                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, DEPLOYMENT_PROCESS_PRIORITY, SipContextFactoryDeploymentProcessor.INSTANCE);

//                final SharedWebMetaDataBuilder sharedWebBuilder = new SharedWebMetaDataBuilder(config.clone());
//                final SharedTldsMetaDataBuilder sharedTldsBuilder = new SharedTldsMetaDataBuilder(config.clone());

//                processorTarget.addDeploymentProcessor(Phase.STRUCTURE, Phase.STRUCTURE_WAR_DEPLOYMENT_INIT, new WarDeploymentInitializingProcessor());
//                processorTarget.addDeploymentProcessor(Phase.STRUCTURE, Phase.STRUCTURE_WAR, new WarStructureDeploymentProcessor(sharedWebBuilder.create(), sharedTldsBuilder));
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_DEPLOYMENT, new WebParsingDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_DEPLOYMENT_FRAGMENT, new WebFragmentParsingDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_JSF_VERSION, new JsfVersionProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_JBOSS_WEB_DEPLOYMENT, new JBossWebParsingDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_TLD_DEPLOYMENT, new TldParsingDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_ANNOTATION_WAR, new WarAnnotationDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_COMPONENTS, new WebComponentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_EAR_CONTEXT_ROOT, new EarContextRootProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_MERGE_METADATA, new WarMetaDataProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.POST_MODULE_JSF_MANAGED_BEANS, new JsfManagedBeanProcessor());
//                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_INITIALIZE_IN_ORDER, new WebInitializeInOrderProcessor(defaultVirtualServer));
//
//                processorTarget.addDeploymentProcessor(Phase.DEPENDENCIES, Phase.DEPENDENCIES_WAR_MODULE, new WarClassloadingDependencyProcessor());
//
//                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, Phase.POST_MODULE_JSF_MANAGED_BEANS, new JsfManagedBeanProcessor());
//                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_SERVLET_INIT_DEPLOYMENT, new ServletContainerInitializerDeploymentProcessor());
//                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_JSF_ANNOTATIONS, new JsfAnnotationProcessor());
//                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_WAR_DEPLOYMENT, new WarDeploymentProcessor(defaultVirtualServer));
            }
        }, OperationContext.Stage.RUNTIME);

//        final SipServerService service = new SipServerService(/*defaultVirtualServer, useNative, */instanceId);
//        newControllers.add(context.getServiceTarget().addService(SipSubsystemServices.JBOSS_SIP, service)
//                .addDependency(AbstractPathService.pathNameOf(TEMP_DIR), String.class, service.getPathInjector())
//                .addDependency(DependencyType.OPTIONAL, ServiceName.JBOSS.append("mbean", "server"), MBeanServer.class, service.getMbeanServer())
//                .setInitialMode(Mode.ON_DEMAND)
//                .install());
//
    }

    @Override
    protected boolean requiresRuntimeVerification() {
        return false;
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return SipSubsystemDescriptions.getSubsystemAddDescription(locale);
    }
}
