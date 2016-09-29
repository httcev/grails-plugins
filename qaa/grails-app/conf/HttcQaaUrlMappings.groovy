class HttcQaaUrlMappings {

	static mappings = {
		name rateAnswer: "/question/$questionId/answer/$answerId/rate"{
			controller = "question"
			action = "rateAnswer"
			constraints {
				// apply constraints here
			}
		}

		name acceptAnswer: "/question/$questionId/answer/$answerId/accept"{
			controller = "question"
			action = "acceptAnswer"
			constraints {
				// apply constraints here
			}
		}
	}
}
