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

## Prerequisites

> **Complete the previous module first.**
> All webhook configuration steps must be done before proceeding:
> [jenkins-module-8.4](https://github.com/explicit-logic/jenkins-module-8.4)

![Webhook configuration](./images/webhook.png)

---

### Install Required Plugin

1. Navigate to **Manage Jenkins** → **Plugins** → **Available Plugins**
2. Search for and install:

**Plugin:** `Ignore Committer Strategy`

> Prevents multibranch pipelines from triggering new builds when commits are made by ignored email addresses — used here to break the CI commit loop.

---

### Pipeline Implementation

The CI pipeline is implemented in [`app/script.groovy`](app/script.groovy) and covers the following steps:

| Step | Description |
|---|---|
| Increment patch version | Bumps the patch segment of the semantic version |
| Build Java application | Compiles the app and removes old build artifacts |
| Build Docker image | Creates a Docker image with a dynamic version tag |
| Push to DockerHub | Publishes the image to a private DockerHub repository |
| Commit version update | Pushes the version bump commit back to Git |

---

## Configure a Multibranch Pipeline

A multibranch pipeline automatically discovers branches and pull requests, creating a separate pipeline job for each.

### Step 1 — Create GitHub Credentials

Jenkins needs a GitHub Personal Access Token to clone the repository and update commit statuses.

**Create the token:**

1. Go to [github.com/settings/tokens/new](https://github.com/settings/tokens/new)
2. Set **Note** to `jenkins`
3. Select the following scopes:

| Scope | Reason |
|---|---|
| `admin:repo_hook` | Create, read, and delete webhooks |
| `public_repo` | Access public repositories |
| `repo:status` | Update commit statuses |

4. Click **Generate token** and copy it immediately

**Add the token to Jenkins:**

1. Navigate to **Manage Jenkins** → **Credentials**
2. Click **Add Credentials** and fill in:

| Field | Value |
|---|---|
| Kind | `Username with password` |
| ID | `github` |
| Username | Your GitHub username *(not your email)* |
| Password | Your personal access token *(starts with `ghp_`)* |

---

### Step 2 — Create the Multibranch Pipeline Job

1. Go to **Dashboard** → **New Item**
2. Enter name `multibranch`, select **Multibranch Pipeline**, click **OK**

**Branch Sources:**

- Click **Add source** → **GitHub**

| Field | Value |
|---|---|
| Credentials | `github` |
| Repository HTTPS URL | `https://github.com/explicit-logic/jenkins-module-8.5` |

- Click **Validate** to confirm access

**Behaviors** — click **Add** and include:
- `Discover branches`
- `Discover pull requests from origin`

**Build Strategies:**

1. Add `Ignore Committer Strategy`
   - **List of author emails to ignore:** `jenkins@example.com`
2. Check **Allow builds when a changeset contains non-ignored author(s)**

> This combination ensures that version-bump commits made by Jenkins do not re-trigger the pipeline, preventing an infinite build loop.

**Build Configuration:**
- Script Path: `Jenkinsfile`

**Scan Multibranch Pipeline Triggers:**
- Check **Periodically if not otherwise run** → set interval to `1 day`

---

### Step 3 — Run the Pipeline

1. Click **Save** — Jenkins will scan the repository and create a job for each branch
2. Push a commit to the repository to trigger the first build

![Autoversion Demo](./images/autoversion.gif)
