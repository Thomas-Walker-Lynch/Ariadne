
; The first time Emacs  encounters a link to a source file, Emacs asks if it should follow it.
; This might suppress that initial question and follow the link.
; (setq find-file-visit-truename t)

(defun jdbx ()
  "Set gud-jdb-sourcepath from the environment and run jdb with the correct source path."
  (interactive)
  (let* 
    (
      (sourcepath (getenv "SOURCEPATH"))
      )
    (if 
      sourcepath
      (setq gud-jdb-sourcepath (split-string sourcepath ":" t))
      (message "Warning: SOURCEPATH is not set. `jdb` will run without source path information.")
      )
    (let 
      (
        (class-name (read-string "Enter the class to debug: " "Test_Util"))
        )
      (jdb (concat "jdb -sourcepath"
             (if 
               sourcepath 
               (mapconcat 'identity gud-jdb-sourcepath ":") ""
               )
             " "
             class-name
             )
        ))))

(defun monitor-jdb-sourcepath (output)
  "Monitor the jdb output for `sourcepath ARG` commands and update `gud-jdb-sourcepath` with each path in ARG."
  (when 
    (string-match "sourcepath \\(.+\\)" output)
    (let* 
      (
        (new-paths (match-string 1 output))
        (paths-list (split-string new-paths ":" t))
        )
      ;; Add each path in paths-list to gud-jdb-sourcepath if not already present
      (dolist 
        (path paths-list)
        (unless 
          (member path gud-jdb-sourcepath)
          (setq gud-jdb-sourcepath (append gud-jdb-sourcepath (list path)))
          )
        )
      (message "Updated gud-jdb-sourcepath: %s" gud-jdb-sourcepath)))
  output)

(add-hook 'gud-filter-functions 'monitor-jdb-sourcepath)
