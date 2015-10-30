## Copyright 2015 JAXIO http://www.jaxio.com
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
##    http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.
##
$output.generateIf($project.isAuditLogPresent())##
$output.java($Audit, "HibernateListenerRegistration")##

$output.requireStatic($Service, "AuditEvent.ApplicationShutdown")##
$output.requireStatic($Service, "AuditEvent.ApplicationStartup")##
$output.requireStatic("org.hibernate.event.spi.EventType.POST_DELETE")##
$output.requireStatic("org.hibernate.event.spi.EventType.POST_INSERT")##
$output.requireStatic("org.hibernate.event.spi.EventType.PRE_UPDATE")##
$output.require("javax.annotation.PostConstruct")##
$output.require("javax.annotation.PreDestroy")##
$output.require("javax.inject.Inject")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.hibernate.SessionFactory")##
$output.require("org.hibernate.event.service.spi.EventListenerRegistry")##
$output.require("org.hibernate.event.spi.EventType")##
$output.require("org.hibernate.internal.SessionFactoryImpl")##
$output.require($Service, "AuditLogService")##

$output.dynamicAnnotationTakeOver("javax.inject.Named", "org.springframework.context.annotation.Lazy(false)")##
public class $output.currentClass {
    private static final Logger log = LoggerFactory.getLogger(${output.currentClass}.class);

    @Inject
    private AuditLogService auditLogService;
    @Inject
    private SessionFactory sessionFactory;
    @Inject
    private AuditLogListener auditLogListener;

    @PostConstruct
    public void registerListeners() {
        register(POST_DELETE, auditLogListener);
        register(POST_INSERT, auditLogListener);
        register(PRE_UPDATE, auditLogListener);
        auditLogService.event(ApplicationStartup);
    }

    private <T> void register(EventType<T> eventType, T t) {
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
        log.info("Registering {} listener on {} events", t.getClass(), eventType);
        registry.getEventListenerGroup(eventType).appendListener(t);
    }

    @PreDestroy
    public void destroy() {
        auditLogService.event(ApplicationShutdown);
    }
}