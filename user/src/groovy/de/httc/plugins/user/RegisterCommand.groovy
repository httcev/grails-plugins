package de.httc.plugins.user

import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.runtime.InvokerHelper

@grails.validation.Validateable
class RegisterCommand {
    String username
    String email
    String password
    String password2
    ProfileCommand profile

    def grailsApplication

    static constraints = {
        username blank: false, validator: { value, command ->
            if (value) {
                def User = command.grailsApplication.getDomainClass(SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
                if (User.findByUsername(value)) {
                    return 'registerCommand.username.unique'
                }
            }
        }
        profile nullable: false, validator: { cmd, obj ->
            if(!cmd.validate()) {
                return 'invalid.profile.message' 
            }
        }
        email blank: false, email: true
        password blank: false, validator: de.httc.plugins.user.RegisterController.passwordValidator
        password2 validator: de.httc.plugins.user.RegisterController.password2Validator
    }

    static {
        RegisterCommand.metaClass.constructor = { Map m ->
           // do work before creation
           def instance = new RegisterCommand()
           instance.profile = new ProfileCommand()
           // do initialization, e.g. "instance.properties = m"
           InvokerHelper.setProperties(instance, m)
           // do work after creation
           return instance
        }
    }
}

@grails.validation.Validateable
class ProfileCommand {
    String firstName
    String lastName

    static constraints = {
        firstName blank: false
        lastName blank: false
    }
}


