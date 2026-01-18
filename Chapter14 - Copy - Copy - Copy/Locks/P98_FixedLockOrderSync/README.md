# Avoiding circular waiting in nested locks
Our code is prone to circular waiting (or deadlock), especially when we use nested locks. Exemplify this scenario and possible fixes.