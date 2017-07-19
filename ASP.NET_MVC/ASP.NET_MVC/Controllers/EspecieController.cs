using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using ASP.NET_MVC.Models;
using System.Data.Entity.Infrastructure;

namespace ASP.NET_MVC.Controllers
{
    public class EspecieController : Controller
    {
        private FarmaVETEntities db = new FarmaVETEntities();

        // GET: /Especie/
        public ActionResult Index()
        {
            return View(db.Especie.ToList().OrderByDescending(e => e.EspecieId));
        }

        // GET: /Especie/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Especie especie = db.Especie.Find(id);
            if (especie == null)
            {
                return HttpNotFound();
            }
            return View(especie);
        }

        // GET: /Especie/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: /Especie/Create
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include="EspecieId,Descripcion")] Especie especie)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.Especie.Add(especie);
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Grabado";
                    return View();
                }
                return View(especie);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionEspecieUK"))
                {
                    ViewBag.MessageError = "Ya existe especie con el mismo nombre";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo grabar registro!";
                }
                return View(especie);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(especie);
            }

        }

        // GET: /Especie/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Especie especie = db.Especie.Find(id);
            if (especie == null)
            {
                return HttpNotFound();
            }
            return View(especie);
        }

        // POST: /Especie/Edit/5
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include="EspecieId,Descripcion")] Especie especie)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.Entry(especie).State = EntityState.Modified;
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Modificado";
                    return View();
                }
                return View(especie);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionEspecieUK"))
                {
                    ViewBag.MessageError = "No puedes editar el nombre a uno existente!";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo editar registro!";
                }
                return View(especie);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(especie);
            }
        }

        // GET: /Especie/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Especie especie = db.Especie.Find(id);
            if (especie == null)
            {
                return HttpNotFound();
            }
            return View(especie);
        }

        // POST: /Especie/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Especie especie = db.Especie.Find(id);
            try
            {
                db.Especie.Remove(especie);
                db.SaveChanges();
                ViewBag.MessageSuccess = "Registro Eliminado";
                return View();
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("FK"))
                {
                    ViewBag.MessageError = "No se puede eliminar porque hay registros enlazados";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo eliminar!";
                }
                return View(especie);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(especie);
            }
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
