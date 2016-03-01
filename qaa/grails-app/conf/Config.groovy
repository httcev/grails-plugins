// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'de.httc.plugins.user.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'de.httc.plugins.user.UserRole'
grails.plugin.springsecurity.authority.className = 'de.httc.plugins.user.Role'
grails.plugin.springsecurity.apf.storeLastUsername = true
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.failureHandler.exceptionMappings = [
   'org.springframework.security.authentication.CredentialsExpiredException': '/user/passwordExpired'
]
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    '/dbconsole/**':     ['permitAll']
]
//grails.plugin.springsecurity.rejectIfNoRule = false
//grails.plugin.springsecurity.fii.rejectPublicInvocations = false

grails.plugin.springsecurity.ui.password.validationRegex='^.*(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$' //example: 1Test@!!
grails.plugin.springsecurity.ui.password.minLength=8
grails.plugin.springsecurity.ui.password.maxLength=64
