package de.httc.plugins.competence.admin

import de.httc.plugins.competence.Competence

import static org.springframework.http.HttpStatus.*
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class CompetencesController {
	static namespace = "admin"
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[competenceList: Competence.list(params), competenceCount: Competence.count()]
	}

	def show(Competence competence) {
		[competence:competence]
	}

	def create() {
		[competence:new Competence(params)]
	}

	@Transactional
	def save(Competence competence) {
		if (competence == null) {
			notFound()
			return
		}

		if (competence.hasErrors()) {
			respond competence.errors, view:'create'
			return
		}

		competence.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.created.message', args: [
					message(code: 'competence.label', default: 'Competence'),
					competence.id
				])
				redirect(action:"show", id:competence.id)
			}
			'*' { respond competence, [status: CREATED] }
		}
	}

	def edit(Competence competence) {
		[competence:competence]
	}

	@Transactional
	def update(Competence competence) {
		if (competence == null) {
			notFound()
			return
		}

		if (competence.hasErrors()) {
			respond competence.errors, view:'edit'
			return
		}

		competence.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.updated.message', args: [
					message(code: 'competence.label', default: 'Competence'),
					competence.id
				])
				redirect(action:"show", id:competence.id)
			}
			'*'{ respond competence, [status: OK] }
		}
	}

	@Transactional
	def delete(Competence competence) {

		if (competence == null) {
			notFound()
			return
		}

		competence.delete flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.deleted.message', args: [
					message(code: 'competence.label', default: 'Competence'),
					competence.id
				])
				redirect action:"list", method:"GET"
			}
			'*'{ render status: NO_CONTENT }
		}
	}

	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [
					message(code: 'competence.label', default: 'Competence'),
					params.id
				])
				redirect action: "list", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
