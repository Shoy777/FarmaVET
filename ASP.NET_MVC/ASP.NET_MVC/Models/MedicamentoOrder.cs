using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ASP.NET_MVC.Models
{
    public class MedicamentoOrder: Medicamento
    {
        [Required]
        public int Cantidad { get; set; }

        [DataType(DataType.Currency)]
        [DisplayFormat(DataFormatString = "{0:C2}", ApplyFormatInEditMode = false)]
        [Display(Name = "Sub Total")]
        public decimal SubTotal { get; set; }
        
        [Display(Name="Especie")]
        public string DescripcionEspecie { get; set; }
        [Display(Name = "Tipo de Medicamento")]
        public string DescripcionTipo { get; set; }

    }
}