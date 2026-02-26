# Module 8 - Build Automation & CI/CD with Jenkins

This repository contains a demo project created as part of my **DevOps studies** in the **TechWorld with Nana – DevOps Bootcamp**.

https://www.techworld-with-nana.com/devops-bootcamp

***Demo Project:*** Dynamically Increment Application version in Jenkins Pipeline

***Technologies used:*** Jenkins, Docker, GitHub, Git, Java, Maven

***Project Description:*** 

- Configure CI step:Increment patch version
- Configure CI step: Build Java application and clean old artifacts
- Configure CI step: Build Image with dynamic Docker Image Tag
- Configure CI step: Push Image to private DockerHub repository
- Configure CI step: Commit version update of Jenkins back to Git repository
- Configure Jenkins pipeline to not trigger automatically on CI build commit to avoid commit loop

---

- Navigate to `Manage Jenkins` -> Plugins -> `Available Plugins`

- Install a jenkins plugin

Name: `Ignore Committer Strategy`

> This plugin provides addition configuration to prevent multi branch projects from triggering new builds based on a list of ignored email addresses.

