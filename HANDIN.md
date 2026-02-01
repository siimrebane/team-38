# HANDIN – Git Collaboration Assignment

## Repository
https://github.com/siimrebane/team-38

---

## Pull Requests

Below is a list of pull requests created as part of the Git collaboration assignment:

1. **Added GitHub username to team list (Anastassia, Svitlana)**  
   https://github.com/siimrebane/team-38/pull/1

2. **Frontend: improve dashboard welcome message; Backend: add price range validation (Svitlana)**  
   https://github.com/siimrebane/team-38/pull/2

3. **Revert "Frontend: improve dashboard welcome message; Backend: add price range validation" (Siim-mentor)**  
   https://github.com/siimrebane/team-38/pull/3

4. **Feature/refactor (Valeria)**  
   https://github.com/siimrebane/team-38/pull/4

5. **Mentor practice: multiple commits and PR workflow (Svitlana)**
   https://github.com/siimrebane/team-38/pull/5

6. **Lighten "View History" button background color (Kristina)**
   https://github.com/siimrebane/team-38/pull/6

7. **Add HANDIN.md file**
   https://github.com/siimrebane/team-38/pull/7

---
## Used merge commit methods

- Two team members (Svitlana, Kristina) created new branches and did multiple commits. Then created pull requests and after review did squash merge.
Squash merge is appropriate here because the commits are small, related, and made for practice purposes. Squashing keeps the main branch history clean while preserving the logical change as a single unit.

- For merge conflict resolution two team members (Kristina, Svitlana) did changes in Team.md file in the same row.
During conflict resolution was used regular merge commit method, that allowed to keep history and that is the more suitable in this situation (two branches, two changes in the same file by different users and merge to common main branch.)

## Merge Conflict Description

### What caused the conflict
A merge conflict occurred in the `TEAM.md` file when two team members (Kristina, Svitlana) modified the same section of the file in separate feature branches.  
Both changes were valid but Git could not automatically decide which version to keep.
Both team members tried to resolve the conflict via IntelliJ IDEA.
Used regular merge commit method.

### How the conflict was resolved
The conflict was resolved using **IntelliJ IDEA** merge conflict resolution tool:

- The conflicting file was opened in the IDEA
- Both versions of the changes were reviewed
- Relevant parts from each side were manually combined
- Conflict markers were removed
- The resolved file was saved, committed, and merged successfully

This ensured that all intended information was preserved correctly.

---

## Team Contributions

### Svitlana
- Updated frontend dashboard welcome message
- Added backend validation for product price ranges
- Created and managed multiple feature branches
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)
- Participated in merge conflict resolution
- Prepared documentation (HANDIN.md)

### Anastassia
- Created initial `TEAM.md` file
- Participated in team coordination and Git collaboration
- Contributed to merge conflict practice
- Created and managed multiple feature branches
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)

### Kristina
- Participated in team coordination and Git collaboration
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)
- Participated in merge conflict resolution
- Frontend changes: Lighten "View History" button background color
- Created and managed multiple feature branches
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)

### Valeria
- Participated in team coordination and Git collaboration
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)
- Frontend refactoring: PR4
- Created and managed multiple feature branches
- Created pull requests and handled merge workflows (tried squash merge and regular merge commits)

---

## Notes
This project was completed as part of a learning assignment focused on:
- Git branching strategies
- Pull request workflows
- Merge types and conflict resolution
- Team collaboration using GitHub
