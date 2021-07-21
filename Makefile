docker_repository ?= 594471699039.dkr.ecr.us-east-1.amazonaws.com/user-default/dispute-workflow
git_commit_sha ?= $(shell git rev-parse HEAD |cut -c1-7)
build_image ?= 594471699039.dkr.ecr.us-east-1.amazonaws.com/mirror/amazoncorretto:11
entrypoint ?= ''
interactive ?=
build_command ?= TERM=dumb ./gradlew build -PbuildProfile=ci --no-daemon
snyk_test_command ?= TERM=dumb ./gradlew snyk-test -PbuildProfile=ci --no-daemon
snyk_monitor_command ?= TERM=dumb ./gradlew snyk-test -PbuildProfile=ci --no-daemon

ci:
	docker run --rm $(interactive) -e JFROG_API_KEY --entrypoint=$(entrypoint) -v $$PWD/:/work -w /work $(build_image) sh -c "$(build_command)"

ci_snyk_test:
	docker run --rm -e JFROG_API_KEY -e SNYK_TOKEN --entrypoint=$(entrypoint) -v $$PWD/:/work -w /work $(build_image) sh -c "$(snyk_test_command)"

ci_snyk_monitor:
	docker run --rm -e JFROG_API_KEY -e SNYK_TOKEN --entrypoint=$(entrypoint) -v $$PWD/:/work -w /work $(build_image) sh -c "$(snyk_monitor_command)"

interactive:
	$(MAKE) ci command='sh' interactive='-it'

docker_build:
	docker build --no-cache --build-arg SOURCE_JAR="./dispute-workflow/build/libs/dispute-workflow.jar" -t $(docker_repository):$(git_commit_sha) -f ./docker/Dockerfile.prebuilt .

docker_push:
	docker push $(docker_repository):$(git_commit_sha)