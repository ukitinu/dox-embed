# dox-embed
## Locates the paragraphs (as in Apache POI) in which a .docx embeddings' are found

This project was born out of necessity: I had to fully parse some *.docx* files, extracting their embedded content too, without losing their positioning.  

Most of my needs were met by the beautiful *Apache POI*, but I soon discovered that, at least in version 4.1.2, there is no way to extract a document's embeddings (`PackagePart`, to maintain Apache POI's parlance) while keeping track of their relative positioning with respect to the paragraphs.  
I was (un)lucky enough to have no simple way to recognise an embedding's position given its content, so I had to play with the documents' xml and this was born.  


Usage is straightforward:
1. execute the .jar (Java 11+ required) specifying the *.docx* document's name;
2. read the file *dox_embeddings*, which lists the indices of the paragraphs containing an embedding.
```
java -jar dox-embed.jar mydocument.docx
```

In the future, apart from improving the code, I would like to see if I can get it integrated into Apache POI, to remove the necessity to use this additional project.
