package de.httc.plugins.user

import grails.plugin.springsecurity.ui.*
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import groovy.text.SimpleTemplateEngine
import grails.plugin.springsecurity.SpringSecurityUtils


class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {
        def index() {
                def copy = [:] + (flash.chainedParams ?: [:])
                copy.remove 'controller'
                copy.remove 'action'
                copy.remove 'format'
                [command: new RegisterCommand(copy)]
        }

	/**
	 * Overridden because default behavior does not work in proxied production environment (it links to private IP)
	**/
	protected String generateLink(String action, linkParams) {
		createLink(controller:"register", action:action, params:linkParams, absolute:true)
	}

        def register(RegisterCommand command) {
println "--- 1"
                if (command.hasErrors()) {
                        render view: 'index', model: [command: command]
                        return
                }
println "--- 2"

                String salt = saltSource instanceof NullSaltSource ? null : command.username
                def user = lookupUserClass().newInstance(email: command.email, username: command.username, profile:[firstName:command.profile.firstName, lastName:command.profile.lastName],
                                accountLocked: true, enabled: true)
                user.save()
println "--- 3"

                RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
println "--- 3.1"
                if (registrationCode == null || registrationCode.hasErrors()) {
println "--- 3.2"

user.errors.allErrors.each {
	println it
}

                        // null means problem creating the user
                        flash.error = message(code: 'spring.security.ui.register.miscError')
                        flash.chainedParams = params
                        redirect action: 'index'
                        return
                }
println "--- 4"

                String url = generateLink('verifyRegistration', [t: registrationCode.token])

                def conf = SpringSecurityUtils.securityConfig
                def body = conf.ui.register.emailBody
                if (body.contains('$')) {
                        body = evaluate(body, [user: user, url: url])
                }
println "--- sending:"
println "server: " + grailsApplication.config.grails.mail.host
println "to: ${command.email}"
println "from: ${conf.ui.register.emailFrom}"
println "subject: ${conf.ui.register.emailSubject}"
println "html: ${body.toString()}"

                mailService.sendMail {
                        to command.email
                        from conf.ui.register.emailFrom
                        subject conf.ui.register.emailSubject
                        html body.toString()
                }
println "--- 6"

                render view: 'index', model: [emailSent: true]
        }

}
