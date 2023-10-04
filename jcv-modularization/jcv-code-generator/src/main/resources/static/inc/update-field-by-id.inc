     @Modifying
     @Query(value = "UPDATE ${schemap}${tabela} SET ${campo} = :${dto}, dt_updated = current_timestamp  WHERE ${pk} = :${pkdto}", nativeQuery = true)
     void update${Adto}ById(@Param("id") Long id, @Param(${classebase}Constantes.${CCdto}) ${tipodart} ${dto});
