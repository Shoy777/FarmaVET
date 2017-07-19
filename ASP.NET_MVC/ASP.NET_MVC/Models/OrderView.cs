using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ASP.NET_MVC.Models
{
    public class OrderView
    {
        [Required]
        public string Cliente { get; set; }

        [DataType(DataType.Currency)]
        [DisplayFormat(DataFormatString = "{0:C2}", ApplyFormatInEditMode = false)]
        public decimal MontoTotal { get; set; }
        
        public MedicamentoOrder Medicamento { get; set; }
        
        public List<MedicamentoOrder> medicamentoOrder { get; set; }
    }
}