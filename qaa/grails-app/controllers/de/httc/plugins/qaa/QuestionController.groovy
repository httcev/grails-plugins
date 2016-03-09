package de.httc.plugins.qaa

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
@Transactional(readOnly = true)
class QuestionController {
  def springSecurityService

  def index(Integer max) {
      params.offset = params.offset && !params.resetOffset ? (params.offset as int) : 0
      params.max = Math.min(max ?: 10, 100)
      params.sort = params.sort ?: "lastUpdated"
      params.order = params.order ?: "desc"

      def user = springSecurityService.currentUser
      def userCompany = user.profile?.company
      def filtered = params.own || params.ownCompany

      def results = Question.createCriteria().list(max:params.max, offset:params.offset) {
          // left join allows null values in the association
          createAlias('creator', 'c', org.hibernate.Criteria.LEFT_JOIN)
          createAlias('c.profile', 'cp', org.hibernate.Criteria.LEFT_JOIN)

          eq("deleted", false)
          if (filtered) {
              or {
                  if (params.own) {
                      eq("creator", user)
                  }
                  if (params.ownCompany) {
                      eq("cp.company", userCompany)
                  }
              }
          }
          order(params.sort, params.order)
      }
      respond results, model:[questionCount:results.totalCount]
  }

  def show(Question question) {
      if (question == null) {
          notFound()
          return
      }
      respond question
  }

  protected void notFound() {
	  flash.message = message(code: 'default.not.found.message', args: [message(code:'de.httc.plugin.qaa.question', default: 'Question'), params.id])
	  redirect action: "index", method: "GET"
  }

  protected void forbidden() {
	  flash.message = message(code: 'default.forbidden.message', args: [message(code:'de.httc.plugin.qaa.question', default: 'Question'), params.id])
	  redirect action: "index", method: "GET"
  }
}
