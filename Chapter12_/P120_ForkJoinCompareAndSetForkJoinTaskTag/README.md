# Tackling `compareAndSetForkJoinTaskTag()`
Consider that you have several interdependent tasks. Provide an implementation based
 on `compareAndSetForkJoinTaskTag()` to avoid multiple calls per task.