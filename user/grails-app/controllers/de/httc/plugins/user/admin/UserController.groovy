package de.httc.plugins.user.admin

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

import de.httc.plugins.user.User
import de.httc.plugins.user.UserRole
import de.httc.plugins.user.Role

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_USER_ADMIN'])
class UserController {
    static namespace = "admin"
    def imageService
    def springSecurityService
    def grailsApplication

    def index(Integer max) {
        params.offset = params.offset ? (params.offset as int) : 0
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: "username"
        params.order = params.order ?: "asc"
        respond User.list(params), model:[userCount: User.count()]
    }

    def create() {
        params.passwordExpired = true
        respond new User(params)
    }

    @Transactional
    def save(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (params['_photo']?.bytes?.length > 0) {
            userInstance.profile.photo = imageService.createThumbnailBytes(params['_photo'].bytes, grailsApplication.mergedConfig.de.httc.plugin.user.avatarSize ?: 40)
        }
        else if (params['_deletePhoto'] == 'true') {
            userInstance.profile.photo = null
        }

        if (!userInstance.save(true)) {
            respond userInstance.errors, view:'create'
            return
        }

        updateRoles(userInstance)

        flash.message = message(code: 'default.created.message', args: [message(code: 'de.httc.plugin.user.user', default: 'User'), userInstance.profile.displayName])
        redirect action:"index", method:"GET"
    }

    def edit(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }
        respond userInstance
    }

    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (params['_photo']?.bytes?.length > 0) {
            userInstance.profile.photo = imageService.createThumbnailBytes(params['_photo'].bytes, grailsApplication.mergedConfig.de.httc.plugin.user.avatarSize ?: 40)
        }
        else if (params['_deletePhoto'] == 'true') {
            userInstance.profile.photo = null
        }

        userInstance.save flush:true
        if (userInstance.hasErrors() || userInstance.profile?.hasErrors()) {
            respond userInstance.errors, view:'edit'
            return
        }
        updateRoles(userInstance)

        flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.user.user', default: 'User'), userInstance.profile.displayName])
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.delete flush:true

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'de.httc.plugin.user.user', default: 'User'), userInstance.profile.displayName])
        redirect action:"index", method:"GET"
    }

    /*
    * copied from https://grails-plugins.github.io/grails-spring-security-core/guide/passwords.html
    */
    @Secured('permitAll')
    def passwordExpired() {
        [username: session['SPRING_SECURITY_LAST_USERNAME']]
    }

    /*
    * copied from https://grails-plugins.github.io/grails-spring-security-core/guide/passwords.html
    */
    @Transactional
    @Secured('permitAll')
    def updatePassword() {
        String username = session['SPRING_SECURITY_LAST_USERNAME']
        if (!username) {
          flash.message = 'Sorry, an error has occurred'
          redirect controller: 'login', action: 'auth'
          return
        }
        String password = params.password
        String newPassword = params.password_new
        String newPassword2 = params.password_new_2
        if (!password || !newPassword || !newPassword2 || newPassword != newPassword2) {
          flash.message = 'Please enter your current password and a valid new password'
          render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
          return
        }

        User user = User.findByUsername(username)
        if (!springSecurityService.passwordEncoder?.isPasswordValid(user.password, password, null /*salt*/)) {
          flash.message = 'Current password is incorrect'
          render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
          return
        }

        if (springSecurityService.passwordEncoder?.isPasswordValid(user.password, newPassword, null /*salt*/)) {
          flash.message = 'Please choose a different password from your current one'
          render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
          return
        }

        user.password = newPassword
        user.passwordExpired = false
        user.save()

        springSecurityService.reauthenticate user.username

        redirect uri:"/"
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.user.user', default: 'User'), params.id])
        redirect action: "index", method: "GET"
    }

    protected void updateRoles(User userInstance) {
        UserRole.findAllByUser(userInstance)*.delete()
        params?.role?.each {
            UserRole.create(userInstance, Role.get(it), true)
        }
    }
}
