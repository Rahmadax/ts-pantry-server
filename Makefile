docker_repository ?= 594471699039.dkr.ecr.us-east-1.amazonaws.com/user-default/dispute-workflow
git_commit_sha ?= $(shell git rev-parse HEAD |cut -c1-7)
build_image ?= 594471699039.dkr.ecr.us-east-1.amazonaws.com/mirror/amazoncorretto:11
entrypoint ?= ''
interactive ?=
command ?= TERM=dumb ./gradlew build -PbuildProfile=ci --no-daemon

ci:
	docker run --rm $(interactive) -e JFROG_API_KEY --entrypoint=$(entrypoint) -v $$PWD/:/work -w /work $(build_image) sh -c "$(command)"

interactive:
	$(MAKE) ci command='sh' interactive='-it'

docker_build:
	docker build --build-arg SOURCE_JAR="./dispute-workflow/build/libs/dispute-workflow.jar" -t $(docker_repository):$(git_commit_sha) -f Dockerfile.prebuilt .

docker_push:
	docker push $(docker_repository):$(git_commit_sha)