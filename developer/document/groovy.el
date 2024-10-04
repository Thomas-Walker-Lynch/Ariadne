
;; Ensure package archives are set up
(require 'package)
(setq package-archives '(("melpa" . "https://melpa.org/packages/")
                         ("gnu" . "https://elpa.gnu.org/packages/")))
(package-initialize)

;; Install neotree if not already installed
(unless (package-installed-p 'groovy-mode)
  (package-refresh-contents)
  (package-install 'groovy-mode))

;; Configure NeoTree
(with-eval-after-load 'groovy-mode
  (setq groovy-indent-offset 2)
  )

;; Enable Groovy mode for files with `#!/usr/bin/env groovy` shebang
(add-to-list 'interpreter-mode-alist '("groovy" . groovy-mode))
