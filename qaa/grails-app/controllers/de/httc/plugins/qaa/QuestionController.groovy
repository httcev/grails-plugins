package de.httc.plugins.qaa

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
@Transactional
class QuestionController {
    def springSecurityService
}
