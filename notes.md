# Publishing
    play publish-local

with snapshot versions

    play publish

with releases and commit to github

# Building in dev mode
play ~ ; reload ; publish-local # module
play ~ test # app